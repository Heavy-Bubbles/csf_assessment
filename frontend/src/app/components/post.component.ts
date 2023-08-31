import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../post.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit{

  fb = inject(FormBuilder)
  postSvc = inject(PostService)
  router = inject(Router)

  postForm!: FormGroup
  tagForm!: FormGroup
  tags: string[] = []

  @ViewChild('photo')
  photo!: ElementRef
  

  ngOnInit(): void {
    this.postForm = this.createForm()
    this.tagForm = this.createTagForm()
  }

  addTags(){
    const string = this.tagForm.value['tags']
    const arrStr = string.split(' ')
    for (let i = 0; i < arrStr.length; i++) {
      if(!this.tags.includes(arrStr[i])){
        this.tags.push(arrStr[i])
      }
    }
  }

  process(){
    const formData = new FormData();
    formData.set('title', this.postForm.value['title'])
    formData.set('photo', this.photo.nativeElement.files[0])
    formData.set('description', this.postForm.value['description'])
    formData.set('tags', JSON.stringify(this.tags))
    this.postSvc.post(formData)
      .then(result => {
        if(result['id'] != null){
          alert(result['id'])
          this.router.navigate(['/'])
          return
        }
        alert(result['message'])
      })
  }

  removeTag(tag: string){
    let idx = this.tags.indexOf(tag)
    this.tags.splice(idx, 1)
  }

  createForm(){
    return this.fb.group(
      { title: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
        photo: this.fb.control('', [Validators.nullValidator]),
        description: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      }
    )
  }

  createTagForm(){
    return this.fb.group({
      tags: this.fb.control<string>('')
    })
  }


}
