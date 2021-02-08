package  com.dm.server.authorization.service.impl;

import  com.dm.server.authorization.entity.Client;
import  com.dm.server.authorization.entity.UserApprovalResult;
import  com.dm.server.authorization.repository.ClientRepository;
import  com.dm.server.authorization.repository.UserApprovalResultRepository;
import com.dm.common.exception.DataNotExistException;
import com.dm.uap.entity.User;
import com.dm.uap.repository.UserRepository;
import org.xyyh.authorization.core.ApprovalResult;
import org.xyyh.authorization.core.ApprovalResultStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.dm.collections.Sets.merge;

@Service
public class UserApprovalStoreServiceImpl implements ApprovalResultStore {

    private final UserApprovalResultRepository approvalResultRepository;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final EntityManager entityManager;

    public UserApprovalStoreServiceImpl(UserApprovalResultRepository approvalResultRepository,
                                        UserRepository userRepository,
                                        ClientRepository clientRepository,
                                        EntityManager entityManager) {
        this.approvalResultRepository = approvalResultRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String clientId, ApprovalResult result) {
        UserApprovalResult uar = approvalResultRepository.findById(username, clientId)
            .orElseGet(UserApprovalResult::new);
        User user = userRepository.findOneByUsernameIgnoreCase(username).orElseThrow(DataNotExistException::new);
        Client client = clientRepository.findById(clientId).orElseThrow(DataNotExistException::new);
        uar.setUser(user);
        uar.setClient(client);
        uar.setExpireAt(result.getExpireAt());
        uar.setScopes(merge(uar.getScopes(), result.getScopes()));
        uar.setRedirectUris(merge(uar.getRedirectUris(), result.getRedirectUris()));
        // Spring data jpa对于持久化这种类型的数据会有bug,调用原生色entityManager
        entityManager.persist(uar);
    }

    @Override
    public Optional<ApprovalResult> get(String userid, String clientId) {
        return approvalResultRepository.findById(userid, clientId).map(UserApprovalResult::toApprovalResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String username, String clientId) {
        userRepository.findOneByUsernameIgnoreCase(username)
            .ifPresent(user -> approvalResultRepository.findById(new UserApprovalResult.Pk(user, clientRepository.getOne(clientId)))
                // 对于这种模型，spring data jpa的删除有问题，需要调用原生的entityManager
                .ifPresent(entityManager::remove)
            );
    }
}
