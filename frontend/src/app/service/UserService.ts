import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
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

  CreateUser(nick: string, name:string, lastname: string,
    email: string, pass: string, userType: number): Observable<HttpResponse<any>>{
      return this.http.post<HttpResponse<any>>('/api/users', {nick, name, lastname, email, pass}, {params: {typeParam: userType.toString()}});
  }

  ApplyManager(id: number): Observable<User>{
    return this.http.put<User>(`api/managers/${id}/applied`, {params: {state: true}});
  }

  LogOut(): Observable<HttpResponse<any>>{  
    return this.http.post<HttpResponse<any>>('/api/logout', {});
  }
}