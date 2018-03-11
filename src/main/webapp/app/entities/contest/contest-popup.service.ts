import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Contest } from './contest.model';
import { ContestService } from './contest.service';

@Injectable()
export class ContestPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private contestService: ContestService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.contestService.find(id)
                    .subscribe((contestResponse: HttpResponse<Contest>) => {
                        const contest: Contest = contestResponse.body;
                        contest.createDate = this.datePipe
                            .transform(contest.createDate, 'yyyy-MM-ddTHH:mm:ss');
                        if (contest.startDate) {
                            contest.startDate = {
                                year: contest.startDate.getFullYear(),
                                month: contest.startDate.getMonth() + 1,
                                day: contest.startDate.getDate()
                            };
                        }
                        if (contest.endDate) {
                            contest.endDate = {
                                year: contest.endDate.getFullYear(),
                                month: contest.endDate.getMonth() + 1,
                                day: contest.endDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.contestModalRef(component, contest);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.contestModalRef(component, new Contest());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    contestModalRef(component: Component, contest: Contest): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.contest = contest;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
