import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'movie',
        loadChildren: () => import('./movie/movie.module').then(m => m.CriteriaMovieModule),
      },
      {
        path: 'actor',
        loadChildren: () => import('./actor/actor.module').then(m => m.CriteriaActorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class CriteriaEntityModule {}
