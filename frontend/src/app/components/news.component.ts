import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from '../models';
import { Subscription } from 'rxjs';
import { PostService } from '../post.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit{

    postSvc = inject(PostService)
    activatedRoute = inject(ActivatedRoute)

    news:  Post[] = []
    tag!: string

    newsSub$!: Subscription

    ngOnInit(): void {
      this.tag = this.activatedRoute.snapshot.params['tag']
      this.newsSub$ = this.postSvc.getNewsByTag(this.tag).subscribe(
        n => {
          console.log(n)
          this.news = n
        }
      )
      console.log(this.news)
    }
}
