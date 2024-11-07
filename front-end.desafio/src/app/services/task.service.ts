import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../models/task';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient) { }

  findById(id: any): Observable<Task> {
    return this.http.get<Task>(`${API_CONFIG.baseUrl}/task/v1/${id}`)
  }

  findAll(): Observable<Task[]> {
    return this.http.get<Task[]>(`${API_CONFIG.baseUrl}/task/v1`);
  }

  create(task: Task): Observable<Task> {
    return this.http.post<Task>(`${API_CONFIG.baseUrl}/task/v1`, task);
  }

  update(task: Task): Observable<Task> {
    return this.http.put<Task>(`${API_CONFIG.baseUrl}/task/v1/${task.id}`, task);
  }

  delete(id: any): Observable<Task> {
    return this.http.delete<Task>(`${API_CONFIG.baseUrl}/task/v1/${id}`)
  }

  updatePresentationOrder(id: any, newOrder: number): Observable<Task> {
    return this.http.patch<Task>(`${API_CONFIG.baseUrl}/task/v1/${id}/${newOrder}`, null);
  }

  launcheGetUrl(url: string): Observable<Task> {
    return this.http.get<Task>(url)
  }
}
