import type { components } from "@/api-types";

export type UserPageDto = components["schemas"]["PageUserDto"];
export type UserDto = components["schemas"]["UserDto"];
export type UserWithStatusDto = components["schemas"]["UserWithStatusDto"];
export type UserShortDto = components["schemas"]["UserShortDto"];
export type UserWithStatusPageDto = components["schemas"]["PageUserWithStatusDto"];
export type NotificationStatusDto = components["schemas"]["NotificationStatusDto"];
export type NotificationHistoryDto = components["schemas"]["NotificationHistoryDto"];
export type PageNotificationHistoryDto = components["schemas"]["PageNotificationHistoryDto"];
export type UiSendNotificationMessageDto = components["schemas"]["UiSendNotificationMessageDto"];

export interface TagStatDto {
  tag: string;
  tagCount: number;
}