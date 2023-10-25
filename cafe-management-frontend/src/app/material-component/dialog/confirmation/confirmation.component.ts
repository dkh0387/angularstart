import {Component, EventEmitter, Inject, Injectable, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

/**
 * Confirmation dialog component.
 * A simple dialog with Yes/No buttons to confirm changes (like change password etc.).
 */
@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {

  onEmitStatusChange = new EventEmitter();
  details: any = {};

  /**
   * Injection token that can be used to access the data that was passed in to a dialog
   * within this component lives.
   * @param dialogData
   */
  constructor(@Inject(MAT_DIALOG_DATA) public dialogData: any) {

  }

  /**
   * Called after the constructor and called after the first ngOnChanges().
   * Mostly, we use ngOnInit for all the initialization/declaration and avoid stuff to work in the constructor.
   * The constructor should only be used to initialize class members but shouldn't do actual "work".
   *
   * So you should use constructor() to set up Dependency Injection and not much else.
   * ngOnInit() is a better place to "start" - it's where/when components' bindings are resolved.
   */
  ngOnInit(): void {
    if (this.dialogData && this.dialogData.confirmation) {
      this.details = this.dialogData;
    }
  }

  handleChangeAction() {
    this.onEmitStatusChange.emit();
  }

}
