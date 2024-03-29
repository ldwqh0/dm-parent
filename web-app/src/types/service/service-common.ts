// eslint-disable-next-line @typescript-eslint/no-empty-interface
export interface Serializable {
  // nothing here
}

export interface ValidationResult {
  success: boolean
  message?: string
}

export interface IdentifiableDto<ID> extends Serializable {
  id?: ID;
}
