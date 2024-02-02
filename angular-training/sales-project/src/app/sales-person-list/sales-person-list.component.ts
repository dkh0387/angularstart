import {Component, OnInit} from '@angular/core';
import {SalesPerson} from "./sales-person";

@Component({
  selector: 'app-sales-person-list',
  templateUrl: './sales-person-list-bootstrap.component.html',
  styleUrls: ['./sales-person-list.component.scss']
})
export class SalesPersonListComponent implements OnInit {

  MIN_SALES_VOLUME: number = 30;

  salesPersonList: SalesPerson[] = [
    new SalesPerson("Denis", "Khaskin", "deniskh87@gmail.com", 23),
    new SalesPerson("Elena", "Khaskina", "khaskina.design@gmail.com", 45)
  ];

  ngOnInit(): void {
  }

  metQuota(salesPerson: SalesPerson) {
    return salesPerson.salesVolume > this.MIN_SALES_VOLUME
  }
}
