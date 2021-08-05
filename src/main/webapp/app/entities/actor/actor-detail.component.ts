import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActor } from 'app/shared/model/actor.model';

@Component({
  selector: 'jhi-actor-detail',
  templateUrl: './actor-detail.component.html',
})
export class ActorDetailComponent implements OnInit {
  actor: IActor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actor }) => (this.actor = actor));
  }

  previousState(): void {
    window.history.back();
  }
}
