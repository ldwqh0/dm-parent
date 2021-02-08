package  com.dm.server.authorization.service.impl;

import  com.dm.server.authorization.converter.ClientConverter;
import  com.dm.server.authorization.dto.ClientDto;
import  com.dm.server.authorization.entity.Client;
import  com.dm.server.authorization.repository.ClientRepository;
import  com.dm.server.authorization.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xyyh.authorization.client.ClientDetails;
import org.xyyh.authorization.client.ClientDetailsService;
import org.xyyh.authorization.exception.NoSuchClientException;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService, ClientDetailsService {

    private final ClientRepository clientRepository;

    private final ClientConverter clientConverter;

    public ClientServiceImpl(ClientRepository clientRepository, ClientConverter clientConverter) {
        this.clientRepository = clientRepository;
        this.clientConverter = clientConverter;
    }

    @Override
    @Transactional
    public Client save(ClientDto app) {
        Client model = Optional.ofNullable(app.getId())
            .map(Client::new)
            .orElseGet(Client::new);
        return clientRepository.save(clientConverter.copyProperties(model, app));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDetails loadClientByClientId(String clientId) throws NoSuchClientException {
        return clientRepository.findById(clientId)
            .map(clientConverter::toClientDetails)
            .orElseThrow(() -> new NoSuchClientException("the client " + clientId + "is not exists"));
    }

    @Override
    public boolean existAnyClient() {
        return clientRepository.count() > 0;
    }

    @Override
    @Transactional
    public void delete(String clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(String id) {
        return clientRepository.findById(id);
    }

    @Override
    @Transactional
    public Client update(String id, ClientDto client) {
        Client c_ = clientRepository.getOne(id);
        c_ = clientConverter.copyProperties(c_, client);
        return clientRepository.save(c_);
    }

    @Override
    public Page<Client> find(String key, Pageable pageable) {
        // TODO 需要增加查询参数
        return clientRepository.findAll(pageable);
    }

}
