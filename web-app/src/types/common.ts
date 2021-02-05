export interface Submitter<T> {
  submit: () => Promise<T>
}

export interface UploadFile {
  state: string,
  file: {
    name: string
  },
  progress: number
}
