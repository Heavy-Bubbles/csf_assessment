import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, firstValueFrom, map } from 'rxjs';
import { Post, TagCount } from './models';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  http = inject(HttpClient)

  post(form: FormData){
    return firstValueFrom(
      this.http.post<any>('/api/post', form)
    )
  }

  getTags(duration: number): Observable<TagCount[]>{
    const params = new HttpParams().set("duration", duration)
    return this.http.get<TagCount[]>("/api/tags", { params })
      .pipe(
        map( tagcount => {
          return tagcount.map(t => {
            return{
              tag: t.tag,
              count: t.count
            } as TagCount
          })
        })
      )
  }

  getNewsByTag(tag: string): Observable<Post[]>{
    const params = new HttpParams().set("tag", tag)
    return this.http.get<Post[]>("/api/news", { params })
    .pipe(
      map( post => {
        return post.map(p => {
          return{
            id: p.id,
            title: p.title,
            postDate: p.postDate,
            photo: p.photo,
            description: p.description,
            tags: p.tags
          } as Post
        })
      })
    )
  }
}
