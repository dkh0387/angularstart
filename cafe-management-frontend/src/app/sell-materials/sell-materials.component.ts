import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {GlobalConstants} from "../shared/global-constants";
import {BuyComponent} from "../material-component/dialog/buy/buy.component";
import {Router} from "@angular/router";
import {Documents} from "../bo/documents";
import {DocumentsService} from "../services/documents.service";

@Component({
    selector: 'app-sell-materials',
    templateUrl: './sell-materials.component.html',
    styleUrls: ['./sell-materials.component.scss']
})
export class SellMaterialsComponent implements OnInit {

    documents: any;

    constructor(private dialog: MatDialog, private router: Router, private documentsService: DocumentsService) {
    }

    ngOnInit(): void {
        this.documents = this.documentsService.getAll();
    }

    handleBuyAction(document: Documents) {
        console.log(document);
        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = GlobalConstants.dialogWidth;
        dialogConfig.data = {documentName: document.name, documentDescription: document.description, documentPrice: document.price};
        const dialogRef = this.dialog.open(BuyComponent, dialogConfig);
        this.router.events.subscribe(() => {
            dialogRef.close();
        });
    }
}
