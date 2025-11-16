import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboard } from './admin-dashboard/admin-dashboard';
import { ManagerDashboard } from './manager-dashboard/manager-dashboard';
import { TechDashboard } from './tech-dashboard/tech-dashboard';
import { ClientDashboard } from './client-dashboard/client-dashboard';

const routes: Routes = [

  {
    path: '',
    redirectTo: 'manager',
    pathMatch: 'full'
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
