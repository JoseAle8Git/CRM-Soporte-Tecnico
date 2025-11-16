import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboard } from './admin-dashboard/admin-dashboard';
import { ManagerDashboard } from './manager-dashboard/manager-dashboard';
import { TechDashboard } from './tech-dashboard/tech-dashboard';
import { ClientDashboard } from './client-dashboard/client-dashboard';
import { ProtectedShell } from './protected-shell/protected-shell';

const routes: Routes = [

  {
    path: '',
    component: ProtectedShell
  },

  {
    path: 'admin',
    component: AdminDashboard
  },

  {
    path: 'manager',
    component: ManagerDashboard
  },

  {
    path: 'tech',
    component: TechDashboard
  },

  {
    path: 'client',
    component: ClientDashboard
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProtectedRoutingModule { }
