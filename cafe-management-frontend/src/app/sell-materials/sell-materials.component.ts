import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {GlobalConstants} from "../shared/global-constants";
import {BuyComponent} from "../material-component/dialog/buy/buy.component";
import {Router} from "@angular/router";

@Component({
    selector: 'app-sell-materials',
    templateUrl: './sell-materials.component.html',
    styleUrls: ['./sell-materials.component.scss']
})
export class SellMaterialsComponent implements OnInit {

    constructor(private dialog: MatDialog, private router: Router) {
    }

    ngOnInit(): void {
    }

    handleBuyAction() {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = GlobalConstants.dialogWidth;
        dialogConfig.data = {document: "document1.pdf"};
        const dialogRef = this.dialog.open(BuyComponent, dialogConfig);
        this.router.events.subscribe(() => {
            dialogRef.close();
        });
    }
}
