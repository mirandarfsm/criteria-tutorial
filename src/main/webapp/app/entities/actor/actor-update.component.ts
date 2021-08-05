import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IActor, Actor } from 'app/shared/model/actor.model';
import { ActorService } from './actor.service';
import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from 'app/entities/movie/movie.service';

@Component({
  selector: 'jhi-actor-update',
  templateUrl: './actor-update.component.html',
})
export class ActorUpdateComponent implements OnInit {
  isSaving = false;
  movies: IMovie[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    movies: [],
  });

  constructor(
    protected actorService: ActorService,
    protected movieService: MovieService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actor }) => {
      this.updateForm(actor);

      this.movieService.query().subscribe((res: HttpResponse<IMovie[]>) => (this.movies = res.body || []));
    });
  }

  updateForm(actor: IActor): void {
    this.editForm.patchValue({
      id: actor.id,
      name: actor.name,
      movies: actor.movies,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const actor = this.createFromForm();
    if (actor.id !== undefined) {
      this.subscribeToSaveResponse(this.actorService.update(actor));
    } else {
      this.subscribeToSaveResponse(this.actorService.create(actor));
    }
  }

  private createFromForm(): IActor {
    return {
      ...new Actor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      movies: this.editForm.get(['movies'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActor>>): void {
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

  trackById(index: number, item: IMovie): any {
    return item.id;
  }

  getSelected(selectedVals: IMovie[], option: IMovie): IMovie {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
