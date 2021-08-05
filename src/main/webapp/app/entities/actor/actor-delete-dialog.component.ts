import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IActor } from 'app/shared/model/actor.model';
import { ActorService } from './actor.service';

@Component({
  templateUrl: './actor-delete-dialog.component.html',
})
export class ActorDeleteDialogComponent {
  actor?: IActor;

  constructor(protected actorService: ActorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.actorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('actorListModification');
      this.activeModal.close();
    });
  }
}
