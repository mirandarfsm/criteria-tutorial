import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from './movie.service';

@Component({
  templateUrl: './movie-delete-dialog.component.html',
})
export class MovieDeleteDialogComponent {
  movie?: IMovie;

  constructor(protected movieService: MovieService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.movieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('movieListModification');
      this.activeModal.close();
    });
  }
}
