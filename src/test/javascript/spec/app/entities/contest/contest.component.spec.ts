/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CloiceTestModule } from '../../../test.module';
import { ContestComponent } from '../../../../../../main/webapp/app/entities/contest/contest.component';
import { ContestService } from '../../../../../../main/webapp/app/entities/contest/contest.service';
import { Contest } from '../../../../../../main/webapp/app/entities/contest/contest.model';

describe('Component Tests', () => {

    describe('Contest Management Component', () => {
        let comp: ContestComponent;
        let fixture: ComponentFixture<ContestComponent>;
        let service: ContestService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloiceTestModule],
                declarations: [ContestComponent],
                providers: [
                    ContestService
                ]
            })
            .overrideTemplate(ContestComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContestComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContestService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Contest(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.contests[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
