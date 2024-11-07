import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatButtonModule } from '@angular/material/button';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TaskListComponent } from './components/task/task-list/task-list.component';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { provideToastr, ToastrModule } from 'ngx-toastr';
import { TaskFormPopupComponent } from './components/task/task-form-popup/task-form-popup.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CurrencyMaskModule } from "ng2-currency-mask";
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';

//import { MatMomentDateModule } from '@angular/material-moment-adapter';
import * as moment from 'moment';

@NgModule({
  declarations: [
    AppComponent,
    TaskListComponent,
    TaskFormPopupComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,

    //Http
    HttpClientModule,

    //Forms
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,

    //Material
    DragDropModule,
    MatButtonModule,
    MatTableModule,
    MatDialogModule,
    MatInputModule,

    //MatPicker
    MatDatepickerModule,
    MatNativeDateModule,
    //MatMomentDateModule,

    //Mask
    CurrencyMaskModule,
    //Toast
    ToastrModule.forRoot({
      timeOut: 4000,
      closeButton: true,
      progressBar: true,
    }),

  ],
  providers: [
    provideToastr(),
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' }
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }

moment.locale('pt-BR');