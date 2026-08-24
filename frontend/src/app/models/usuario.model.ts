export type Role = 'GARCOM' | 'CAIXA' | 'ADMIN';

export interface Usuario {
  id: number;
  username: string;
  role: Role;
}
