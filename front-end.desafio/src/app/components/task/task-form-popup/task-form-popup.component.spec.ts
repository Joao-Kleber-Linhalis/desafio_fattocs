import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskFormPopupComponent } from './task-form-popup.component';

describe('TaskFormPopupComponent', () => {
  let component: TaskFormPopupComponent;
  let fixture: ComponentFixture<TaskFormPopupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaskFormPopupComponent]
    });
    fixture = TestBed.createComponent(TaskFormPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
