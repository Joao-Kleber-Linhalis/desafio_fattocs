import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Task } from 'src/app/models/task';
import * as moment from 'moment';
import { TaskService } from 'src/app/services/task.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-task-form-popup',
  templateUrl: './task-form-popup.component.html',
  styleUrls: ['./task-form-popup.component.css']
})
export class TaskFormPopupComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<TaskFormPopupComponent>,
    private service: TaskService,
    private toast: ToastrService,) { }
  moment = moment;
  minDate: Date = new Date();
  inputData: any;
  isCreate: boolean = true;

  task: Task = {
    name: '',
    cost: null!,
    limitDate: this.moment().toISOString(),
  }

  cost: FormControl = new FormControl(null, [
    Validators.required,
    Validators.min(0.01)
  ]);

  name: FormControl = new FormControl(null, Validators.minLength(3));

  limitDate = new FormControl(moment(), Validators.required);

  ngOnInit(): void {
    this.inputData = this.data
    console.log(this.inputData)
    if (this.inputData.task != null) {
      this.findById(this.inputData.task)
      this.isCreate = false;
    }
    console.log(this.task.limitDate)
  }

  findById(id: any) {
    this.service.findById(this.inputData.task).subscribe({
      next: (response) => {
        this.task = response
        const limitDateString = this.task.limitDate;

        const limitDateMoment = moment(limitDateString);
        console.log(limitDateMoment.toISOString())

        this.limitDate.setValue(limitDateMoment);
        console.log(this.limitDate.value)
      },
      error: () => {
        this.closeDialog("Erro")
      }
    });
  }



  onDateSelected(event: any) {
    if (event.value) {
      this.task.limitDate = this.moment(event.value).toISOString();
      console.log(this.task.limitDate)
    } else {
      this.task.limitDate = this.moment().format('DD/MM/YYYY');
    }
  }


  closeDialog(type: String = "Cancel") {
    this.dialogRef.close({ title: this.inputData.title, type: type });
  }

  save() {
    if (this.validarCampos()) {
      const request = this.isCreate ? this.service.create(this.task) : this.service.update(this.task);

      request.subscribe({
        complete: () => {
          this.closeDialog("Sucesso");
        },
        error: (e) => {
          const action = this.isCreate ? "criação" : "atualização";
          this.toast.error(e, `Erro na ${action}`);
        }
      });
    } else {
      this.toast.info("Campos necessários em branco", "Info");
    }
  }

  validarCampos(): boolean {
    return this.name.valid && this.cost.valid && this.limitDate.valid
  }
}

moment.locale('pt-BR');