import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CriteriaTestModule } from '../../../test.module';
import { MovieUpdateComponent } from 'app/entities/movie/movie-update.component';
import { MovieService } from 'app/entities/movie/movie.service';
import { Movie } from 'app/shared/model/movie.model';

describe('Component Tests', () => {
  describe('Movie Management Update Component', () => {
    let comp: MovieUpdateComponent;
    let fixture: ComponentFixture<MovieUpdateComponent>;
    let service: MovieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CriteriaTestModule],
        declarations: [MovieUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MovieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MovieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MovieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Movie(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Movie();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
