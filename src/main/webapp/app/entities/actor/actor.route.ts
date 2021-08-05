import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IActor, Actor } from 'app/shared/model/actor.model';
import { ActorService } from './actor.service';
import { ActorComponent } from './actor.component';
import { ActorDetailComponent } from './actor-detail.component';
import { ActorUpdateComponent } from './actor-update.component';

@Injectable({ providedIn: 'root' })
export class ActorResolve implements Resolve<IActor> {
  constructor(private service: ActorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((actor: HttpResponse<Actor>) => {
          if (actor.body) {
            return of(actor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Actor());
  }
}

export const actorRoute: Routes = [
  {
    path: '',
    component: ActorComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'criteriaApp.actor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActorDetailComponent,
    resolve: {
      actor: ActorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'criteriaApp.actor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActorUpdateComponent,
    resolve: {
      actor: ActorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'criteriaApp.actor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActorUpdateComponent,
    resolve: {
      actor: ActorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'criteriaApp.actor.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
