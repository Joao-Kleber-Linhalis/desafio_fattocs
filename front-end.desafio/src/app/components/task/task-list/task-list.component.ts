import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Task } from 'src/app/models/task';
import { ToastrService } from 'ngx-toastr';
import { TaskService } from 'src/app/services/task.service';



@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {

  constructor(
    private service: TaskService,
    private toast: ToastrService,
  ) { }

  @ViewChild('table', { static: true })
  table!: MatTable<Task>;

  ELEMENT_DATA: Task[] = [];

  displayedColumns: string[] = ['name', 'cost', 'limitDate'];
  dataSource = this.ELEMENT_DATA;

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.service.findAll().subscribe({
      next: (response) => {
        this.ELEMENT_DATA = response
        this.dataSource = this.ELEMENT_DATA;
      },
      error: (e) => {
        this.toast.error("Erro no Carregamento das Tarefas", "ERRO");
        console.log(e)
      },
      complete: () => this.toast.success("Carregamento de tarefas concluído", "Concluído")
    })
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);

    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Mês começa em 0
    const year = date.getFullYear();

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }


  drop(event: CdkDragDrop<string>) {
    const previousIndex = this.dataSource.findIndex(d => d === event.item.data);
    

    moveItemInArray(this.dataSource, previousIndex, event.currentIndex);

    this.table.renderRows();
  }

}
