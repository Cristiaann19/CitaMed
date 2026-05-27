import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth-service';
import { LoaderService } from '../../../core/services/loader-service';


@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})

export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';
  verPassword: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private loaderService: LoaderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      usuario: ['', [Validators.required, Validators.minLength(3)]],
      clave: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loaderService.show();
      this.errorMessage = '';

      const { usuario, clave } = this.loginForm.value;

      this.authService.login(usuario, clave).subscribe({
        next: (response) => {
          this.loaderService.hide();
          if (response.perfil === 'MEDICO') {
            this.router.navigate(['/admin/citas']);
          } else {
            this.router.navigate(['/admin/dashboard']);
          }
        },
        error: (err) => {
          this.loaderService.hide();
          this.errorMessage = 'Usuario o contraseña incorrectos';
          console.error('Error en login:', err);
        }
      });
    }
  }

  togglePassword(): void {
    this.verPassword = !this.verPassword;
  }
}