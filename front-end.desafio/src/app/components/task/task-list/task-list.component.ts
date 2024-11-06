import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
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
  dataSource = new MatTableDataSource<Task>(this.ELEMENT_DATA);  // Agora é uma instância de MatTableDataSource

  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.service.findAll().subscribe({
      next: (response) => {
        this.ELEMENT_DATA = response;
        this.dataSource.data = this.ELEMENT_DATA;  // Atualiza dataSource.data
      },
      error: (e) => {
        this.toast.error("Erro no Carregamento das Tarefas", "ERRO");
        console.log(e);
      },
      complete: () => this.toast.success("Carregamento de tarefas concluído", "Concluído")
    });
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

  updatePresentationOrder(id: any, newOrder: number): Promise<void> {
    return new Promise((resolve, reject) => {
      this.service.updatePresentationOrder(id, newOrder).subscribe({
        complete: () => {
          this.toast.success("Alteração de prioridade salva", "Alterado");
          resolve();
        },
        error: (e) => {
          this.toast.error("Erro durante alteração de prioridade", "ERRO");
          console.log(e);
          reject(e);
        }
      });
    });
  }

  async drop(event: CdkDragDrop<string>) {
    const previousIndex = this.dataSource.data.findIndex(d => d === event.item.data);
    const taskIndex = this.dataSource.data[previousIndex]?.id;  // Agora dataSource.data é acessível
    console.log(taskIndex);

    try {
      await this.updatePresentationOrder(taskIndex, event.currentIndex + 1);

      moveItemInArray(this.dataSource.data, previousIndex, event.currentIndex);
      this.table.renderRows();
    } catch (error) {
      console.log("Não foi possível atualizar a prioridade:", error);
    }
  }
}
