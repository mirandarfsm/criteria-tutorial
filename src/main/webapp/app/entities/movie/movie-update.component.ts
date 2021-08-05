import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMovie, Movie } from 'app/shared/model/movie.model';
import { MovieService } from './movie.service';

@Component({
  selector: 'jhi-movie-update',
  templateUrl: './movie-update.component.html',
})
export class MovieUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    year: [],
    subject: [],
    popularity: [],
    awards: [],
  });

  constructor(protected movieService: MovieService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movie }) => {
      this.updateForm(movie);
    });
  }

  updateForm(movie: IMovie): void {
    this.editForm.patchValue({
      id: movie.id,
      title: movie.title,
      year: movie.year,
      subject: movie.subject,
      popularity: movie.popularity,
      awards: movie.awards,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const movie = this.createFromForm();
    if (movie.id !== undefined) {
      this.subscribeToSaveResponse(this.movieService.update(movie));
    } else {
      this.subscribeToSaveResponse(this.movieService.create(movie));
    }
  }

  private createFromForm(): IMovie {
    return {
      ...new Movie(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      year: this.editForm.get(['year'])!.value,
      subject: this.editForm.get(['subject'])!.value,
      popularity: this.editForm.get(['popularity'])!.value,
      awards: this.editForm.get(['awards'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovie>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
