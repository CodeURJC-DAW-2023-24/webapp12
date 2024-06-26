import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';

@Component({
    selector: 'app-editProfile',
    templateUrl: './editProfile.component.html',
})
export class EditProfileComponent {
    title = 'frontend';
    public user!: User;
    public userId!: number;
    public imageUrl!: string;
    public selectedFile: File;

    constructor(private userService: UserService, private router: Router,
        private route: ActivatedRoute, public loginService: LoginService) {
        this.selectedFile = new File([], '');

        this.route.params.subscribe(params => {
            this.userId = params['userId'];
        });

    }

    ngOnInit() {
        this.userService.getUserById(this.userId).subscribe({
            next: (user: User) => {
                this.user = user;
                this.imageUrl = `/api/users/${user.id}/image`;
            },
            error: (err: HttpErrorResponse) => {
                if (err.status === 403) {
                    console.log('Forbidden error');
                    this.router.navigate(['/error']);

                } else {
                    console.log('Unknown error');
                    this.router.navigate(['/error']);
                }
            }
        });
    }

    fileSelected(event: Event) {
        event.preventDefault();
        const inputElement = event.target as HTMLInputElement;
        if (inputElement.files && inputElement.files.length > 0) {
            this.selectedFile = inputElement.files[0];
        }
    }

    updateUser(name: string, lastname: string, location: string, org: string,
        language: string, phone: string, mail: string, bio: string): void {
        let updates: { [key: string]: string } = {};

        if (name !== this.user.name) {
            updates['name'] = name;
        }
        if (lastname !== this.user.lastname) {
            updates['lastname'] = lastname;
        }
        if (location !== this.user.location) {
            updates['location'] = location;
        }
        if (org !== this.user.organization) {
            updates['organization'] = org;
        }
        if (language !== this.user.language) {
            updates['language'] = language;
        }
        if (phone !== this.user.phone) {
            updates['phone'] = phone;
        }
        if (mail !== this.user.email) {
            updates['email'] = mail;
        }
        if (bio !== this.user.bio) {
            updates['bio'] = bio;
        }

        let formData = new FormData();
        for (const key in updates) {
            if (updates.hasOwnProperty(key)) {
                formData.append(key, updates[key]);
            }
        }

        this.userService.updateUserDetails(this.user.id, formData).subscribe({
            next: () => {
                this.router.navigate(['/profile']);
            },
            error: err => {
                if (err.status === 400) {
                    console.error('Error updating user details: Exception originated from JSON data processing or mapping', err);
                } else if (err.status === 403) {
                    console.error('Error updating user details: Operation not allowed for the current user', err);
                } else if (err.status === 404) {
                    console.error('Error updating user details: User not found', err);
                } else {
                    console.error('Error updating user details', err);
                }
                this.router.navigate(['/error']);
            }
      });
    }

    editProfileImage(id: number, file: File) {
        if (file && file.size > 0) {
            this.userService.editProfileImage(id, file).subscribe({
            next: _ => {
                this.imageUrl = `/api/users/${this.user.id}/image?${new Date().getTime()}`;
            },
            error: err => {
                if (err.status === 400) {
                    console.error('Error updating user details: Exception originated from JSON data processing or mapping', err);
                } else if (err.status === 403) {
                    console.error('Error updating user details: Operation not allowed for the current user', err);
                } else if (err.status === 404) {
                    console.error('Error updating user details: User not found', err);
                } else {
                    console.error('Error updating user details', err);
                }
                this.router.navigate(['/error']);
            }
      });
    }else{
        console.error('No image selected');
    }
    }
}
