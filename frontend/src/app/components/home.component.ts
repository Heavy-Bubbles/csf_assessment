import { Component, OnChanges, OnInit, SimpleChanges, ViewChild, inject } from '@angular/core';
import { PostService } from '../post.service';
import { Subscription } from 'rxjs';
import { TagCount } from '../models';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{

  postSvc = inject(PostService)

  tags: TagCount[] = []

  tagSub$!: Subscription

  selectedDuration = 0

  selectDuration(duration: string){
    this.selectedDuration = Number(duration)
    this.postSvc.getTags(this.selectedDuration)
    this.tagSub$.unsubscribe
    this.tagSub$ = this.postSvc.getTags(this.selectedDuration).subscribe(
      t => {
        this.tags = t
      }
    )
  }

  ngOnInit(){
    this.tagSub$ = this.postSvc.getTags(5).subscribe(
      t => {
        this.tags = t
      }
    )
  }
}
