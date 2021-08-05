import { Subject } from 'app/shared/model/enumerations/subject.model';

export interface IMovie {
  id?: number;
  title?: string;
  year?: number;
  subject?: Subject;
  popularity?: number;
  awards?: boolean;
}

export class Movie implements IMovie {
  constructor(
    public id?: number,
    public title?: string,
    public year?: number,
    public subject?: Subject,
    public popularity?: number,
    public awards?: boolean
  ) {
    this.awards = this.awards || false;
  }
}
