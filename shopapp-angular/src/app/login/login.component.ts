import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { BaseComponent } from '../base/base.component';
import { Role } from '../models/role';
import { UserResponse } from '../responses/user/user.response';
import { ApiResponse } from '../responses/api.response';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginDTO } from '../dtos/user/login.dto';
import { catchError, finalize, of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends BaseComponent implements OnInit{
  @ViewChild('loginForm') loginForm!: NgForm;  
    

  /*
  //Login user1
  phoneNumber: string = '33445566';
  password: string = '123456789';

  //Login user2
  phoneNumber: string = '0964896239';
  password: string = '123456789';


  //Login admin
  phoneNumber: string = '11223344';
  password: string = '11223344';

  */
  phoneNumber: string = '33445566';
  password: string = '123456789';
  showPassword: boolean = false;

  roles: Role[] = []; // Mảng roles
  rememberMe: boolean = true;
  selectedRole: Role | undefined; // Biến để lưu giá trị được chọn từ dropdown
  userResponse?: UserResponse

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`);
    //how to validate ? phone must be at least 6 characters
  }
  

  ngOnInit() {
    // Gọi API lấy danh sách roles và lưu vào biến roles
    debugger
    this.roleService.getRoles().subscribe({
      // next: ({ data: roles }: ApiResponse) => {
        next: ( roles : any) => {
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi tải danh sách vai trò',
          title: 'Lỗi Tải Vai Trò'
        });
      }
    });    
  }
  trackByRoleName(index: number, role: any) {
    return role.name;
  }
  createAccount() {
    // Chuyển hướng người dùng đến trang đăng ký (hoặc trang tạo tài khoản)
    this.router.navigate(['/register']); 
  }
  loginWithGoogle() {    
    debugger
    this.authService.authenticate('google').subscribe({
      next: (url: string) => {
        debugger
        // Chuyển hướng người dùng đến URL đăng nhập Google
        window.location.href = url;
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi kết nối với Google',
          title: 'Lỗi Đăng Nhập'
        });
      }
    });
  }  
  
  loginWithFacebook() {         
    // Logic đăng nhập với Facebook
    debugger
    this.authService.authenticate('facebook').subscribe({
      next: (url: string) => {
        debugger
        // Chuyển hướng người dùng đến URL đăng nhập Facebook
        window.location.href = url;
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi kết nối với Facebook',
          title: 'Lỗi Đăng Nhập'
        });
      }
    });
  }
  
  // login() {
  //   const loginDTO: LoginDTO = {
  //     phone_number: this.phoneNumber,
  //     password: this.password,
  //     role_id: this.selectedRole?.id ?? 1
  //   };
  
  //   this.userService.login(loginDTO).pipe(
  //     tap((apiResponse: ApiResponse) => {
  //       const { token } = apiResponse.data;
  //       this.tokenService.setToken(token);
  //     }),
  //     switchMap((apiResponse: ApiResponse) => {
  //       const { token } = apiResponse.data;
  //       return this.userService.getUserDetail(token).pipe(
  //         tap((apiResponse2: ApiResponse) => {
  //           this.userResponse = {
  //             ...apiResponse2.data,
  //             date_of_birth: new Date(apiResponse2.data.date_of_birth),
  //           };
  
  //           if (this.rememberMe) {
  //             this.userService.saveUserResponseToLocalStorage(this.userResponse);
  //           }
  
  //           if (this.userResponse?.role.name === 'admin') {
  //             this.router.navigate(['/admin']);
  //           } else if (this.userResponse?.role.name === 'user') {
  //             this.router.navigate(['/']);
  //           }
  //         }),
  //         catchError((error: HttpErrorResponse) => {
  //           console.error('Lỗi khi lấy thông tin người dùng:', error?.error?.message ?? '');
  //           return of(null); // Tiếp tục chuỗi Observable
  //         })
  //       );
  //     }),
  //     finalize(() => {
  //       this.cartService.refreshCart();
  //     })
  //   ).subscribe({
  //     error: (error: HttpErrorResponse) => {
  //       this.toastService.showToast({
  //         error: error,
  //         defaultMsg: 'Sai thông tin đăng nhập',
  //         title: 'Lỗi Đăng Nhập'
  //       });
  //     }
  //   });
  // }
  login() {
    const message = `phone: ${this.phoneNumber}` +
                    ` password: ${this.password}`;
    //alert(message);
    debugger;
  
    let loginDTO: LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password,
      "role_id":this.selectedRole?.id ?? 1
    }
  
    this.userService.login(loginDTO).subscribe({
      next: (response: any) => {
        const {token} = response;
        this.tokenService.setToken(token);
        //this.router.navigate(['/login']);
      },
      complete: () => {
      },
      error: (error: any) => {
        alert(`Cannot register, error: ${error.error}`)
      }
    });
  }
  
  
  togglePassword() {
    this.showPassword = !this.showPassword;
  }
}

