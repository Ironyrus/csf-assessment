import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit{

  cuisine!: Restaurant[];

	// TODO Task 2
	// For View 1
  constructor(private restService: RestaurantService,
              private router: Router) {}

  ngOnInit() {
    this.restService.getCuisineList().subscribe((data) => {
      // console.log(data);
      this.cuisine = data;
    });
  }

  onSubmit(route: any){
    this.router.navigate(['cuisines', route.cuisine]);
  }
}
