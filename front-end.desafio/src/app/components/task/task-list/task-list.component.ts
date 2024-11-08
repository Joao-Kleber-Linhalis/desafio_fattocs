import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Task } from 'src/app/models/task';
import { ToastrService } from 'ngx-toastr';
import { TaskService } from 'src/app/services/task.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { TaskFormPopupComponent } from '../task-form-popup/task-form-popup.component';
import { API_CONFIG } from 'src/app/config/api.config';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {

  constructor(
    private service: TaskService,
    private toast: ToastrService,
    private dialogRef: MatDialog,
  ) { }

  @ViewChild('table', { static: true })
  table!: MatTable<Task>;

  ELEMENT_DATA: Task[] = [];
  displayedColumns: string[] = ['name', 'cost', 'limitDate', 'actions'];
  dataSource = new MatTableDataSource<Task>(this.ELEMENT_DATA);

  ngOnInit(): void {
    this.findAll();
  }

  findAll(needToast: boolean = true) {
    this.service.findAll().subscribe({
      next: (response) => {
        this.ELEMENT_DATA = response;
        this.dataSource.data = this.ELEMENT_DATA;
      },
      error: (e) => {
        this.toast.error("Erro no Carregamento das Tarefas", "ERRO");
        console.log(e);
      },
      complete: () => {
        if (needToast) { this.toast.success("Carregamento de tarefas concluído", "Concluído") }
      }
    });
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

  delete(id: any, name: string) {
    if (confirm("Deseja excluir a tarefa: " + name)) {
      this.service.delete(id).subscribe({
        complete: () => {
          this.toast.success("Tarefa excluída com sucesso", "Exclusão");
          this.findAll(false);
        },
        error: (e) => {
          this.toast.error("Erro durante exclusão de tarefa", "ERRO");
          console.log(e);
        }
      });
    }
  }


  openFormDialog(task?: Task) {
    const dialogConfig = new MatDialogConfig;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "25%";
    dialogConfig.height = '400px';
    dialogConfig.data = {
      title: task != null ? "Edição" : "Cadastro",
      task: task?.id
    }
    var _popup = this.dialogRef.open(TaskFormPopupComponent, dialogConfig);
    _popup.afterClosed().subscribe(close => {
      if (!close?.type) {
        this.toast.info("Formulário fechado pelo usuário", "Formulário")
      }
      else if (close.type === null || close.type === "Cancel") {
        this.toast.info(`${close.title} de tarefa cancelada`, close.title)
      }
      else if (close.type === "Error") {
        this.toast.error(`Erro na ${close.title}`, "Erro")

      }
      else {
        this.toast.success(`${close.title} de tarefa concluída`, close.title)
      }
      this.findAll(false)
    })
  }

  async drop(event: CdkDragDrop<string>) {
    const previousIndex = this.dataSource.data.findIndex(d => d === event.item.data);
    const taskIndex = this.dataSource.data[previousIndex]?.id;
    console.log(taskIndex);

    try {
      await this.updatePresentationOrder(taskIndex, event.currentIndex + 1);

      moveItemInArray(this.dataSource.data, previousIndex, event.currentIndex);
      this.table.renderRows();
    } catch (error) {
      console.log("Não foi possível atualizar a prioridade:", error);
    }
  }

  getSwaggerUrl(): string {
    return API_CONFIG.baseUrl.replace("api", '') + 'swagger-ui.html';
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Mês começa em 0
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }
}
