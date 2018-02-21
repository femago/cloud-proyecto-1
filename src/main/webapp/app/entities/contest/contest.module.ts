import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloiceSharedModule } from '../../shared';
import { CloiceAdminModule } from '../../admin/admin.module';
import {
    ContestService,
    ContestPopupService,
    ContestComponent,
    ContestDetailComponent,
    ContestDialogComponent,
    ContestPopupComponent,
    ContestDeletePopupComponent,
    ContestDeleteDialogComponent,
    contestRoute,
    contestPopupRoute,
    ContestResolvePagingParams,
} from './';
import {CloiceApplicationModule} from '../application/application.module';

const ENTITY_STATES = [
    ...contestRoute,
    ...contestPopupRoute,
];

@NgModule({
    imports: [
        CloiceSharedModule,
        CloiceAdminModule,
        CloiceApplicationModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContestComponent,
        ContestDetailComponent,
        ContestDialogComponent,
        ContestDeleteDialogComponent,
        ContestPopupComponent,
        ContestDeletePopupComponent,
    ],
    entryComponents: [
        ContestComponent,
        ContestDialogComponent,
        ContestPopupComponent,
        ContestDeleteDialogComponent,
        ContestDeletePopupComponent,
    ],
    providers: [
        ContestService,
        ContestPopupService,
        ContestResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloiceContestModule {}
