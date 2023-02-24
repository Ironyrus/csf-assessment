import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit{
	
  constructor(
    private service: RestaurantService,
    private route: ActivatedRoute,
    private router: Router) {}

  data!: string;
  cuisine!: Restaurant[];

  ngOnInit() {
    this.route.params.subscribe((data) => {
      this.data = data['cuisine'];
      this.data.trim;
    })

    const result = this.service.getRestaurantsByCuisine(this.data).subscribe((data) =>{
      this.cuisine = data;
      console.log(data)
    })
  }

	// TODO Task 3
	// For View 2

  onSubmit(route: any) {
    this.router.navigate(['restaurants', route.name])
  }
}
