import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ContestComponent } from './contest.component';
import { ContestDetailComponent } from './contest-detail.component';
import { ContestPopupComponent } from './contest-dialog.component';
import { ContestDeletePopupComponent } from './contest-delete-dialog.component';

@Injectable()
export class ContestResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const contestRoute: Routes = [
    {
        path: 'contest',
        component: ContestComponent,
        resolve: {
            'pagingParams': ContestResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloiceApp.contest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contest/:id',
        component: ContestDetailComponent,
        resolve: {
            'pagingParams': ContestResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloiceApp.contest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contestPopupRoute: Routes = [
    {
        path: 'contest-new',
        component: ContestPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloiceApp.contest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contest/:id/edit',
        component: ContestPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloiceApp.contest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contest/:id/delete',
        component: ContestDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloiceApp.contest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
