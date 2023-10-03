import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject  } from 'rxjs';
import { Constants } from '../model/constants';
import { Customers } from '../model/customers'
import { Accounts } from '../model/accounts';
import { CurrencyConversion } from '../model/currencyconversion';


@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  
  
  accountsUrl = Constants.accountsUrl;
  ordersUrl = Constants.ordersUrl;
  customerUrl = Constants.customerUrl
  LoginUrl = Constants.loginUrl
  loggedIn: boolean = false;
  adminLoggedIn: boolean = false
  customerLoggedIn: boolean = false
  datanotfound:boolean=false
  //email: any;
  edit: boolean = false;
  deleteEmail: any;
  id: any;
  accountEdit: boolean = false;
  customerEdit: boolean = false;
  customerAccountEdit: boolean = false
  customerId: any
  customerIdforExchange: any

  constructor(private httpclient: HttpClient) { }
 

  postingCustomerdetails(data: Customers): Observable<any> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    console.log("posting customers-", localStorage.getItem("token"))
    return this.httpclient.post<any>(this.customerUrl + "create", data, { headers })
  }
  getCustomers(): Observable<Customers[]> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    return this.httpclient.get<Customers[]>(this.customerUrl + "fetchAll", { headers })
  }
  deleteCustomer(id: number): Observable<Customers[]> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    return this.httpclient.delete<Customers[]>(this.customerUrl + "delete/" + id, { headers })
  }
  savingAccountsToCustomer(data: Object, id: any): Observable<Accounts> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    console.log(data)
    return this.httpclient.post<Accounts>(this.accountsUrl + "create/" + id, data, { headers })
  }
  getAccounts(data: number): Observable<Accounts> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    console.log("accessing accounts-", localStorage.getItem("token"));
    return this.httpclient.get<Accounts>(this.accountsUrl + "fetchAll/" + data, { headers })
  }
  getAccountsforCustomer(data: any): Observable<Accounts> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    console.log("accessing accounts-", localStorage.getItem("token"));
    return this.httpclient.get<Accounts>(this.accountsUrl + "getAll/" + data, { headers })
  }
  deleteAccount(data: any, id: any): Observable<Accounts[]> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    return this.httpclient.delete<Accounts[]>(this.accountsUrl + "delete/" + data + "/" + id, { headers })
  }
  editCustomer(data: Customers, id: any): Observable<Customers> {
    console.log("edit customer-", localStorage.getItem("token"));
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    return this.httpclient.put<Customers>(this.customerUrl + "update/" + id, data, { headers })
  }
  editAccount(data: Object, id: any, accid: any): Observable<Accounts[]> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    console.log(id, accid);
    return this.httpclient.put<Accounts[]>(this.accountsUrl + "update/" + id + "/" + accid, data, { headers })
  }
  login(data: any): Observable<any> {
    console.log("asaas");
    console.log(this.LoginUrl);
    
    
    return this.httpclient.post(this.LoginUrl, data)
  }

  currencyConversion(data:CurrencyConversion): Observable<CurrencyConversion> {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    return this.httpclient.post<CurrencyConversion>(this.ordersUrl + "placeorder", data, { headers })
  }
  orderWatchlist() {
    const headers = { 'Authorization': 'Bearer ' + localStorage.getItem("token") }
    return this.httpclient.get(this.ordersUrl + "watchlist", { headers })
  }

  getLiveCurrencyRate(orderFromCurrencyType :string ): Observable<String> { 
    const apiresponse= this.httpclient.get<String>("https://open.er-api.com/v6/latest/"+orderFromCurrencyType)
    return apiresponse
  }
}
