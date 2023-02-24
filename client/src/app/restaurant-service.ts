import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom, Observable, take } from 'rxjs'
import { Restaurant, Comment } from './models'

@Injectable({
    providedIn: 'root'
})

export class RestaurantService {

	constructor(private http: HttpClient) {}

	// TODO Task 2 
	// Use the following method to get a list of cuisines
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getCuisineList():Observable<Restaurant[]> {
		// Implememntation in here
		const url = "http://localhost:8080/api/cuisines"
		const headers = new HttpHeaders()
            .set('content-type', 'application/json')
            .set('Access-Control-Allow-Origin', '*');
		const result = this.http.get<any>(url, {headers: headers});
		return result;
	}

	// TODO Task 3 
	// Use the following method to get a list of restaurants by cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getRestaurantsByCuisine(data: string):Observable<Restaurant[]> {
		// Implememntation in here
		const url = "http://localhost:8080/api/" + data + "/restaurants";
		const url2 = "http://localhost:8080/api/restaurants"

		const headers = new HttpHeaders()
			.set('content-type', 'application/json')
            .set('Access-Control-Allow-Origin', '*');
		const result = this.http.get<any>(url, {headers: headers});
		return result;
	}
	
	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public getRestaurant(name:string): Promise<Restaurant> {
	// 	// Implememntation in here
	const url: string = "http://localhost:8080/api/restaurants/" + name;
	const queryParams: HttpParams = new HttpParams();
	const headers = new HttpHeaders()
		.set('content-type', 'application/json')
		.set('Access-Control-Allow-Origin', '*'); // Very important 
	const result$ = this.http.get<any>(url, {params: queryParams, headers: headers}).pipe(take(1));
	return lastValueFrom(result$);	
	}

	// TODO Task 5
	// Use this method to submit a comment
	// DO NOT CHANGE THE METHOD'S NAME OR SIGNATURE
	public postComment(comment: Comment): Promise<any> {
	// // 	// Implememntation in here
	const url: string = "http://localhost:8080/api/comments";
	const queryParams: HttpParams = new HttpParams();
	const headers = new HttpHeaders()
		.set('content-type', 'application/json')
		.set('Access-Control-Allow-Origin', '*'); // Very important 
	const result$ = this.http.post<Comment>(url, comment, {params: queryParams, headers: headers}).pipe(take(1));
	return lastValueFrom(result$);	
	}
}
