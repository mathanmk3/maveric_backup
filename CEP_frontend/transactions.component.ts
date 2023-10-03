import { Component, OnInit } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { CustomerService } from 'src/app/service/customer.service';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {
  display: boolean = false;
  transactions:any
  name:any
  userName:any
  constructor(public service:CustomerService){
    const customer:any=localStorage.getItem('userName')
   this.name=JSON.parse(customer)
   this.userName=this.name.split("@")
  }
 
  totalRecords:any
  pageNumber:any=1;

  ngOnInit(): void {
    this.service.orderWatchlist().pipe(catchError((error:any) => {
      if (error.status === 403) {
        this.service.datanotfound=true
        this.display=true
      }
    return throwError(error)})).subscribe(x=>{
      if(x==null){
       this.service.datanotfound=true
       this.display=true
      }else{
        this.display=false
      }
      this.transactions=x
      this.totalRecords=this.transactions.length
      console.log(x);
      
    },(err)=>{

    })
  }

}
