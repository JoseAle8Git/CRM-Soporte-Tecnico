import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'roleDisplay',
  standalone: true
})
export class RoleDisplayPipe implements PipeTransform {

  // Transforma la clave del rol en nombre legible.
  transform(value: string | undefined): string {
    
    if(!value) {
      return 'N/A';
    }

    switch (value.toUpperCase()) {
      case 'ADMIN':
        return 'Administrador';
      case 'MANAGER':
        return 'Gerente';
      case 'TECH':
        return 'Técnico'
      case 'CLIENT':
        return 'Cliente';
      default:
        return value;
    }

  }

}
