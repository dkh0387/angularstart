import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent implements OnInit {

  displayedColumns: string[] = ['name', 'edit'];

  constructor() {
  }

  ngOnInit(): void {
  }

}
