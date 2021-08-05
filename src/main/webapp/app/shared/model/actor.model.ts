import { IMovie } from 'app/shared/model/movie.model';

export interface IActor {
  id?: number;
  name?: string;
  movies?: IMovie[];
}

export class Actor implements IActor {
  constructor(public id?: number, public name?: string, public movies?: IMovie[]) {}
}
