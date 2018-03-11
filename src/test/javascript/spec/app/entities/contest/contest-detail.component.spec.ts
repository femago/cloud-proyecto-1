/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { CloiceTestModule } from '../../../test.module';
import { ContestDetailComponent } from '../../../../../../main/webapp/app/entities/contest/contest-detail.component';
import { ContestService } from '../../../../../../main/webapp/app/entities/contest/contest.service';
import { Contest } from '../../../../../../main/webapp/app/entities/contest/contest.model';

describe('Component Tests', () => {

    describe('Contest Management Detail Component', () => {
        let comp: ContestDetailComponent;
        let fixture: ComponentFixture<ContestDetailComponent>;
        let service: ContestService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloiceTestModule],
                declarations: [ContestDetailComponent],
                providers: [
                    ContestService
                ]
            })
            .overrideTemplate(ContestDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContestDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContestService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Contest(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.contest).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
