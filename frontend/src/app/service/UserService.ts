import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  Login(nickname: string, pass: string) : Observable<string>{
    return this.http.post<string>('/api/login', {nickname, pass});

    
  } 
}