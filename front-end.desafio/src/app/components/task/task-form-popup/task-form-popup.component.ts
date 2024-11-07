import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Task } from 'src/app/models/task';
import * as moment from 'moment';

@Component({
  selector: 'app-task-form-popup',
  templateUrl: './task-form-popup.component.html',
  styleUrls: ['./task-form-popup.component.css']
})
export class TaskFormPopupComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<TaskFormPopupComponent>,) { }
  moment = moment;
  minDate: Date = new Date();
  inputData: any;

  task: Task = {
    name: '',
    cost: 0,
    limitDate: this.moment().format('DD/MM/YYYY'),
  }


  ngOnInit(): void {
    this.inputData = this.data
    if (this.inputData.task != null) {
      this.task = this.inputData.task
    }
  }


  cost: FormControl = new FormControl(null, [
    Validators.required,
    Validators.min(0.01)
  ]);

  name: FormControl = new FormControl(null, Validators.minLength(3));

  limitDate = new FormControl(moment(), Validators.required);

  onDateSelected(event: any) {
    if (event.value) {
      this.task.limitDate = this.moment(event.value).format('DD/MM/YYYY');
      console.log("task" + this.task.limitDate);
    } else {
      this.task.limitDate = this.moment().format('DD/MM/YYYY');
    }
  }
  closeDialog(type: String = "Cancel") {
    this.dialogRef.close({ title: this.inputData.title, type: type });
  }
}

moment.locale('pt-BR');