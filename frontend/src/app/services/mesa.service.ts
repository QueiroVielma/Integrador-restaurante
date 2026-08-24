import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Mesa } from '../models/mesa.model';

@Injectable({ providedIn: 'root' })
export class MesaService {
  private readonly url = `${environment.apiUrl}/mesas`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Mesa[]> {
    return this.http.get<Mesa[]>(this.url);
  }
}
