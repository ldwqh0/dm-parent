import { Serializable } from '@/types/service/service-authorization'

export interface IdentifiableDto<ID> extends Serializable {
  id?: ID;
}
