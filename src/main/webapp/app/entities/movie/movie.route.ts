import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMovie, Movie } from 'app/shared/model/movie.model';
import { MovieService } from './movie.service';
import { MovieComponent } from './movie.component';
import { MovieDetailComponent } from './movie-detail.component';
import { MovieUpdateComponent } from './movie-update.component';

@Injectable({ providedIn: 'root' })
export class MovieResolve implements Resolve<IMovie> {
  constructor(private service: MovieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMovie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((movie: HttpResponse<Movie>) => {
          if (movie.body) {
            return of(movie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Movie());
  }
}

export const movieRoute: Routes = [
  {
    path: '',
    component: MovieComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'criteriaApp.movie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MovieDetailComponent,
    resolve: {
      movie: MovieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'criteriaApp.movie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MovieUpdateComponent,
    resolve: {
      movie: MovieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'criteriaApp.movie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MovieUpdateComponent,
    resolve: {
      movie: MovieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'criteriaApp.movie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
