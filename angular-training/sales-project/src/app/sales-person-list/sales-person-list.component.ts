import {Component, OnInit} from '@angular/core';
import {SalesPerson} from "./sales-person";

@Component({
  selector: 'app-sales-person-list',
  templateUrl: './sales-person-list.component.html',
  styleUrls: ['./sales-person-list.component.scss']
})
export class SalesPersonListComponent implements OnInit {

  salesPersonList: SalesPerson[] = [
    new SalesPerson("Denis", "Khaskin", "deniskh87@gmail.com", 23),
    new SalesPerson("Elena", "Khaskina", "khaskina.design@gmail.com", 45)
  ];

  ngOnInit(): void {
  }


}
