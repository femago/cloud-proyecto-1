import { BaseEntity, User } from './../../shared';

export class Contest implements BaseEntity {
    constructor(
        public id?: number,
        public createDate?: any,
        public name?: string,
        public logoContentType?: string,
        public logo?: any,
        public uuid?: string,
        public startDate?: any,
        public endDate?: any,
        public price?: number,
        public script?: any,
        public notes?: string,
        public applications?: BaseEntity[],
        public user?: User,
    ) {
    }
}
