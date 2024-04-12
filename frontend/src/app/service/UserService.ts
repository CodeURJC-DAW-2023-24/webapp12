import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../entities/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  Login(username: string, password: string) : Observable<string>{
    return this.http.post<string>('/api/login', {username, password});  
  } 

  GetCurrentUser(): Observable<User>{
    return this.http.get<User>('/api/currentUser');
    
  }
}