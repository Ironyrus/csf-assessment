import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';
import { Comment } from '../models';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit{
	
  restaurantForm!: FormGroup;
  name!: string;
  cuisine!: string;
  address!: string;
  data!: string;
  model!: Restaurant;
  mapUrl!: String;
  restaurant_id!: string;
  commentModel!:Comment;

  constructor(private service: RestaurantService,
              private route: ActivatedRoute,
              private fb: FormBuilder) {}

  ngOnInit() {
    this.route.params.subscribe((data) => {
      this.data = data['name'];
      this.data.trim;
    })
    this.service.getRestaurant(this.data).then((data: any) => {
      this.model = data;
      this.mapUrl = data.mapUrl;
      this.restaurant_id = data.restaurant_id;
      console.log(this.model);
    }).catch((error) => {
      console.log(error)
    });

    this.restaurantForm = this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      rating: this.fb.control<string>('', [Validators.required]),
      comment: this.fb.control<string>('', [Validators.required])
    })
  }
	// TODO Task 4 and Task 5
	// For View 3

  onSubmit() {
    const model: Comment = {
      name: this.restaurantForm.get('name')?.value,
      rating: this.restaurantForm.get('rating')?.value,
      restaurantId: this.restaurant_id,
      text: this.restaurantForm.get('comment')?.value
    };

    this.service.postComment(model).then((data: any) => {
      console.log(data)
    }).catch((error)=> {
      console.log(error)
    });

  }
}
